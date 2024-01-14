import { Either } from '../../../shared/domain/either';
import { NotFoundError } from '../../../shared/domain/errors/not-found.error';
import { Profile, ProfileId } from '../../domain/profile.aggregate';
import { IProfileRepository } from '../../domain/profile.repository';

export class ProfilesIdExistsInDatabaseValidator {
  constructor(private categoryRepo: IProfileRepository) {}

  async validate(
    profiles_id: string[],
  ): Promise<Either<ProfileId[], NotFoundError[]>> {
    const profilesId = profiles_id.map((v) => new ProfileId(v));

    const existsResult = await this.categoryRepo.existsById(profilesId);
    return existsResult.not_exists.length > 0
      ? Either.fail(
          existsResult.not_exists.map((c) => new NotFoundError(c.id, Profile)),
        )
      : Either.ok(profilesId);
  }
}
