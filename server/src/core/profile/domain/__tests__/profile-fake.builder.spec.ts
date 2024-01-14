import { Chance } from 'chance';
import { ProfileFakeBuilder } from '../profile-fake.builder';
import { ProfileId } from '../profile.aggregate';

describe('ProfileFakerBuilder Unit Tests', () => {
  describe('profile_id prop', () => {
    const faker = ProfileFakeBuilder.aProfile();

    test('should throw error when any with methods has called', () => {
      expect(() => faker.profile_id).toThrowError(
        new Error("Property profile_id not have a factory, use 'with' methods"),
      );
    });

    test('should be undefined', () => {
      expect(faker['_profile_id']).toBeUndefined();
    });

    test('withProfileId', () => {
      const profile_id = new ProfileId();
      const $this = faker.withProfileId(profile_id);
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_profile_id']).toBe(profile_id);

      faker.withProfileId(() => profile_id);
      //@ts-expect-error _profile_id is a callable
      expect(faker['_profile_id']()).toBe(profile_id);

      expect(faker.profile_id).toBe(profile_id);
    });

    //TODO - melhorar este nome
    test('should pass index to profile_id factory', () => {
      let mockFactory = jest.fn(() => new ProfileId());
      faker.withProfileId(mockFactory);
      faker.build();
      expect(mockFactory).toHaveBeenCalledTimes(1);

      const profileId = new ProfileId();
      mockFactory = jest.fn(() => profileId);
      const fakerMany = ProfileFakeBuilder.theProfiles(2);
      fakerMany.withProfileId(mockFactory);
      fakerMany.build();

      expect(mockFactory).toHaveBeenCalledTimes(2);
      expect(fakerMany.build()[0].profile_id).toBe(profileId);
      expect(fakerMany.build()[1].profile_id).toBe(profileId);
    });
  });

  describe('name prop', () => {
    const faker = ProfileFakeBuilder.aProfile();
    test('should be a function', () => {
      expect(typeof faker['_name']).toBe('function');
    });

    test('should call the word method', () => {
      const chance = Chance();
      const spyWordMethod = jest.spyOn(chance, 'word');
      faker['chance'] = chance;
      faker.build();

      expect(spyWordMethod).toHaveBeenCalled();
    });

    test('withName', () => {
      const $this = faker.withName('test name');
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_name']).toBe('test name');

      faker.withName(() => 'test name');
      //@ts-expect-error name is callable
      expect(faker['_name']()).toBe('test name');

      expect(faker.name).toBe('test name');
    });

    test('should pass index to name factory', () => {
      faker.withName((index) => `test name ${index}`);
      const profile = faker.build();
      expect(profile.name).toBe(`test name 0`);

      const fakerMany = ProfileFakeBuilder.theProfiles(2);
      fakerMany.withName((index) => `test name ${index}`);
      const profiles = fakerMany.build();

      expect(profiles[0].name).toBe(`test name 0`);
      expect(profiles[1].name).toBe(`test name 1`);
    });

    test('invalid too long case', () => {
      const $this = faker.withInvalidNameTooLong();
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_name'].length).toBe(256);

      const tooLong = 'a'.repeat(256);
      faker.withInvalidNameTooLong(tooLong);
      expect(faker['_name'].length).toBe(256);
      expect(faker['_name']).toBe(tooLong);
    });
  });

  describe('description prop', () => {
    const faker = ProfileFakeBuilder.aProfile();
    test('should be a function', () => {
      expect(typeof faker['_description']).toBe('function');
    });

    test('should call the paragraph method', () => {
      const chance = Chance();
      const spyParagraphMethod = jest.spyOn(chance, 'paragraph');
      faker['chance'] = chance;
      faker.build();
      expect(spyParagraphMethod).toHaveBeenCalled();
    });

    test('withDescription', () => {
      const $this = faker.withDescription('test description');
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_description']).toBe('test description');

      faker.withDescription(() => 'test description');
      //@ts-expect-error description is callable
      expect(faker['_description']()).toBe('test description');

      expect(faker.description).toBe('test description');
    });

    test('should pass index to description factory', () => {
      faker.withDescription((index) => `test description ${index}`);
      const profile = faker.build();
      expect(profile.description).toBe(`test description 0`);

      const fakerMany = ProfileFakeBuilder.theProfiles(2);
      fakerMany.withDescription((index) => `test description ${index}`);
      const profiles = fakerMany.build();

      expect(profiles[0].description).toBe(`test description 0`);
      expect(profiles[1].description).toBe(`test description 1`);
    });
  });

  describe('is_active prop', () => {
    const faker = ProfileFakeBuilder.aProfile();
    test('should be a function', () => {
      expect(typeof faker['_is_active']).toBe('function');
    });

    test('activate', () => {
      const $this = faker.activate();
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_is_active']).toBe(true);
      expect(faker.is_active).toBe(true);
    });

    test('deactivate', () => {
      const $this = faker.deactivate();
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_is_active']).toBe(false);
      expect(faker.is_active).toBe(false);
    });
  });

  describe('created_at prop', () => {
    const faker = ProfileFakeBuilder.aProfile();

    test('should throw error when any with methods has called', () => {
      const fakerProfile = ProfileFakeBuilder.aProfile();
      expect(() => fakerProfile.created_at).toThrowError(
        new Error("Property created_at not have a factory, use 'with' methods"),
      );
    });

    test('should be undefined', () => {
      expect(faker['_created_at']).toBeUndefined();
    });

    test('withCreatedAt', () => {
      const date = new Date();
      const $this = faker.withCreatedAt(date);
      expect($this).toBeInstanceOf(ProfileFakeBuilder);
      expect(faker['_created_at']).toBe(date);

      faker.withCreatedAt(() => date);
      //@ts-expect-error _created_at is a callable
      expect(faker['_created_at']()).toBe(date);
      expect(faker.created_at).toBe(date);
    });

    test('should pass index to created_at factory', () => {
      const date = new Date();
      faker.withCreatedAt((index) => new Date(date.getTime() + index + 2));
      const profile = faker.build();
      expect(profile.created_at.getTime()).toBe(date.getTime() + 2);

      const fakerMany = ProfileFakeBuilder.theProfiles(2);
      fakerMany.withCreatedAt((index) => new Date(date.getTime() + index + 2));
      const profiles = fakerMany.build();

      expect(profiles[0].created_at.getTime()).toBe(date.getTime() + 2);
      expect(profiles[1].created_at.getTime()).toBe(date.getTime() + 3);
    });
  });

  test('should create a profile', () => {
    const faker = ProfileFakeBuilder.aProfile();
    let profile = faker.build();

    expect(profile.profile_id).toBeInstanceOf(ProfileId);
    expect(typeof profile.name === 'string').toBeTruthy();
    expect(typeof profile.description === 'string').toBeTruthy();
    expect(profile.is_active).toBe(true);
    expect(profile.created_at).toBeInstanceOf(Date);

    const created_at = new Date();
    const profile_id = new ProfileId();
    profile = faker
      .withProfileId(profile_id)
      .withName('name test')
      .withDescription('description test')
      .deactivate()
      .withCreatedAt(created_at)
      .build();

    expect(profile.profile_id.id).toBe(profile_id.id);
    expect(profile.name).toBe('name test');
    expect(profile.description).toBe('description test');
    expect(profile.is_active).toBe(false);
    expect(profile.created_at).toBe(created_at);
  });

  test('should create many profiles', () => {
    const faker = ProfileFakeBuilder.theProfiles(2);
    let profiles = faker.build();

    profiles.forEach((profile) => {
      expect(profile.profile_id).toBeInstanceOf(ProfileId);
      expect(typeof profile.name === 'string').toBeTruthy();
      expect(typeof profile.description === 'string').toBeTruthy();
      expect(profile.is_active).toBe(true);
      expect(profile.created_at).toBeInstanceOf(Date);
    });

    const created_at = new Date();
    const profile_id = new ProfileId();
    profiles = faker
      .withProfileId(profile_id)
      .withName('name test')
      .withDescription('description test')
      .deactivate()
      .withCreatedAt(created_at)
      .build();

    profiles.forEach((profile) => {
      expect(profile.profile_id.id).toBe(profile_id.id);
      expect(profile.name).toBe('name test');
      expect(profile.description).toBe('description test');
      expect(profile.is_active).toBe(false);
      expect(profile.created_at).toBe(created_at);
    });
  });
});
