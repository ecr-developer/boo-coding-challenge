import { Profile, ProfileId } from '../profile.aggregate';

describe('Profile Without Validator Unit Tests', () => {
  beforeEach(() => {
    Profile.prototype.validate = jest
      .fn()
      .mockImplementation(Profile.prototype.validate);
  });
  test('constructor of Profile', () => {
    const expectProfile = {
      name: 'A Martinez',
      description: 'Adolph Larrue Martinez III.',
      mbti: 'ISFJ',
      enneagram: '9w3',
      variant: 'sp/so',
      tritype: 725,
      socionics: 'SEE',
      sloan: 'RCOEN',
      psyche: 'FEVL',
      image: 'https://soulverse.boo.world/images/1.png',
    };

    let profile = new Profile({
      name: expectProfile.name,
      description: expectProfile.description,
      mbti: expectProfile.mbti,
      enneagram: expectProfile.enneagram,
      variant: expectProfile.variant,
      tritype: expectProfile.tritype,
      socionics: expectProfile.socionics,
      sloan: expectProfile.sloan,
      psyche: expectProfile.psyche,
      image: expectProfile.image,
    });

    expect(profile.profile_id).toBeInstanceOf(ProfileId);
    expect(profile.description).toBe(expectProfile.description);
    expect(profile.mbti).toBe(expectProfile.mbti);
    expect(profile.enneagram).toBe(expectProfile.enneagram);
    expect(profile.variant).toBe(expectProfile.variant);
    expect(profile.tritype).toBe(expectProfile.tritype);
    expect(profile.socionics).toBe(expectProfile.socionics);
    expect(profile.sloan).toBe(expectProfile.sloan);
    expect(profile.psyche).toBe(expectProfile.psyche);
    expect(profile.image).toBe(expectProfile.image);
    expect(profile.is_active).toBe(true);
    expect(profile.created_at).toBeInstanceOf(Date);

    const created_at = new Date();
    profile = new Profile({
      name: expectProfile.name,
      description: 'some description',
      mbti: expectProfile.mbti,
      enneagram: expectProfile.enneagram,
      variant: expectProfile.variant,
      tritype: expectProfile.tritype,
      socionics: expectProfile.socionics,
      sloan: expectProfile.sloan,
      psyche: expectProfile.psyche,
      image: expectProfile.image,
      is_active: false,
      created_at,
    });

    expect(profile.profile_id).toBeInstanceOf(ProfileId);
    expect(profile.description).toBe('some description');
    expect(profile.mbti).toBe(expectProfile.mbti);
    expect(profile.enneagram).toBe(expectProfile.enneagram);
    expect(profile.variant).toBe(expectProfile.variant);
    expect(profile.tritype).toBe(expectProfile.tritype);
    expect(profile.socionics).toBe(expectProfile.socionics);
    expect(profile.sloan).toBe(expectProfile.sloan);
    expect(profile.psyche).toBe(expectProfile.psyche);
    expect(profile.image).toBe(expectProfile.image);
    expect(profile.is_active).toBe(false);
    expect(profile.created_at).toBe(created_at);
  });

  describe('create command', () => {
    test('should create a profile', () => {
      const expectProfile = {
        name: 'A Martinez',
        description: 'Adolph Larrue Martinez III.',
        mbti: 'ISFJ',
        enneagram: '9w3',
        variant: 'sp/so',
        tritype: 725,
        socionics: 'SEE',
        sloan: 'RCOEN',
        psyche: 'FEVL',
        image: 'https://soulverse.boo.world/images/1.png',
      };

      const profile = Profile.create({
        name: expectProfile.name,
        description: expectProfile.description,
        mbti: expectProfile.mbti,
        enneagram: expectProfile.enneagram,
        variant: expectProfile.variant,
        tritype: expectProfile.tritype,
        socionics: expectProfile.socionics,
        sloan: expectProfile.sloan,
        psyche: expectProfile.psyche,
        image: expectProfile.image,
      });

      expect(profile.profile_id).toBeInstanceOf(ProfileId);
      expect(profile.description).toBe(expectProfile.description);
      expect(profile.mbti).toBe(expectProfile.mbti);
      expect(profile.enneagram).toBe(expectProfile.enneagram);
      expect(profile.variant).toBe(expectProfile.variant);
      expect(profile.tritype).toBe(expectProfile.tritype);
      expect(profile.socionics).toBe(expectProfile.socionics);
      expect(profile.sloan).toBe(expectProfile.sloan);
      expect(profile.psyche).toBe(expectProfile.psyche);
      expect(profile.image).toBe(expectProfile.image);
      expect(profile.is_active).toBe(true);
      expect(profile.created_at).toBeInstanceOf(Date);
      expect(Profile.prototype.validate).toHaveBeenCalledTimes(1);
    });
  });
});
