import { NotFoundError } from '../../../shared/domain/errors/not-found.error';
import { Profile, ProfileId } from '../../domain/profile.aggregate';
import { ProfileInMemoryRepository } from '../../infra/db/in-memory/profile-in-memory.repository';
import { ProfilesIdExistsInDatabaseValidator } from './profiles-ids-exists-in-database.validator';

describe('ProfilesIdExistsInDatabaseValidator Unit Tests', () => {
  let profileRepo: ProfileInMemoryRepository;
  let validator: ProfilesIdExistsInDatabaseValidator;
  beforeEach(() => {
    profileRepo = new ProfileInMemoryRepository();
    validator = new ProfilesIdExistsInDatabaseValidator(profileRepo);
  });

  it('should return many not found error when profiles id is not exists in storage', async () => {
    const profileId1 = new ProfileId();
    const profileId2 = new ProfileId();
    const spyExistsById = jest.spyOn(profileRepo, 'existsById');
    let [profilesId, errorsProfilesId] = await validator.validate([
      profileId1.id,
      profileId2.id,
    ]);
    expect(profilesId).toStrictEqual(null);
    expect(errorsProfilesId).toStrictEqual([
      new NotFoundError(profileId1.id, Profile),
      new NotFoundError(profileId2.id, Profile),
    ]);

    expect(spyExistsById).toHaveBeenCalledTimes(1);

    const profile1 = Profile.fake().aProfile().build();
    await profileRepo.insert(profile1);

    [profilesId, errorsProfilesId] = await validator.validate([
      profile1.profile_id.id,
      profileId2.id,
    ]);
    expect(profilesId).toStrictEqual(null);
    expect(errorsProfilesId).toStrictEqual([
      new NotFoundError(profileId2.id, Profile),
    ]);
    expect(spyExistsById).toHaveBeenCalledTimes(2);
  });

  it('should return a list of categories id', async () => {
    const profile1 = Profile.fake().aProfile().build();
    const profile2 = Profile.fake().aProfile().build();
    await profileRepo.bulkInsert([profile1, profile2]);
    const [profilesId, errorsProfilesId] = await validator.validate([
      profile1.profile_id.id,
      profile2.profile_id.id,
    ]);
    expect(profilesId).toHaveLength(2);
    expect(errorsProfilesId).toStrictEqual(null);
    expect(profilesId[0]).toBeValueObject(profile1.profile_id);
    expect(profilesId[1]).toBeValueObject(profile2.profile_id);
  });
});
