import { ISearchableRepository } from '../../shared/domain/repository/repository-interface';
import { SearchParams } from '../../shared/domain/repository/search-params';
import { SearchResult } from '../../shared/domain/repository/search-result';
import { Profile, ProfileId } from './profile.aggregate';

export type ProfileFilter = string;

export class ProfileSearchParams extends SearchParams<ProfileFilter> {}

export class ProfileSearchResult extends SearchResult<Profile> {}

export interface IProfileRepository
  extends ISearchableRepository<
    Profile,
    ProfileId,
    ProfileFilter,
    ProfileSearchParams,
    ProfileSearchResult
  > {}
