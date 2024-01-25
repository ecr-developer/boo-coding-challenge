import {
  PaginationOutput,
  PaginationOutputMapper,
} from '../../../../shared/application/pagination-output';
import { IUseCase } from '../../../../shared/application/use-case.interface';
import { SortDirection } from '../../../../shared/domain/repository/search-params';
import {
  ProfileFilter,
  ProfileSearchParams,
  ProfileSearchResult,
  IProfileRepository,
} from '../../../domain/profile.repository';
import { ProfileOutput, ProfileOutputMapper } from '../common/profile-output';

export class ListProfilesUseCase
  implements IUseCase<ListProfilesInput, ListProfilesOutput>
{
  constructor(private profileRepo: IProfileRepository) {}

  async execute(input: ListProfilesInput): Promise<ListProfilesOutput> {
    const params = new ProfileSearchParams(input);
    const searchResult = await this.profileRepo.search(params);
    return this.toOutput(searchResult);
  }

  private toOutput(searchResult: ProfileSearchResult): ListProfilesOutput {
    const { items: _items } = searchResult;
    const items = _items.map((i) => {
      return ProfileOutputMapper.toOutput(i);
    });
    return PaginationOutputMapper.toOutput(items, searchResult);
  }
}

export type ListProfilesInput = {
  page?: number;
  per_page?: number;
  sort?: string | null;
  sort_dir?: SortDirection | null;
  filter?: ProfileFilter | null;
};

export type ListProfilesOutput = PaginationOutput<ProfileOutput>;
