import { SortDirection } from '../../../../shared/domain/repository/search-params';
import { InMemorySearchableRepository } from '../../../../shared/infra/db/in-memory/in-memory.repository';
import { Profile, ProfileId } from '../../../domain/profile.aggregate';
import {
  ProfileFilter,
  IProfileRepository,
} from '../../../domain/profile.repository';

export class ProfileInMemoryRepository
  extends InMemorySearchableRepository<Profile, ProfileId>
  implements IProfileRepository
{
  sortableFields: string[] = ['name', 'created_at'];

  protected async applyFilter(
    items: Profile[],
    filter: ProfileFilter | null,
  ): Promise<Profile[]> {
    if (!filter) {
      return items;
    }

    return items.filter((i) => {
      return i.name.toLowerCase().includes(filter.toLowerCase());
    });
  }
  getEntity(): new (...args: any[]) => Profile {
    return Profile;
  }

  protected applySort(
    items: Profile[],
    sort: string | null,
    sort_dir: SortDirection | null,
  ) {
    return sort
      ? super.applySort(items, sort, sort_dir)
      : super.applySort(items, 'created_at', 'desc');
  }
}
