import { Profile } from '../../../domain/profile.aggregate';
import { ProfileOutputMapper } from './profile-output';

describe('ProfileOutputMapper Unit Tests', () => {
  it('should convert a profile in output', () => {
    const entity = Profile.create({
      name: 'A Martinez',
      description: 'some description',
      is_active: true,
    });
    const spyToJSON = jest.spyOn(entity, 'toJSON');
    const output = ProfileOutputMapper.toOutput(entity);
    expect(spyToJSON).toHaveBeenCalled();
    expect(output).toStrictEqual({
      id: entity.profile_id.id,
      name: 'A Martinez',
      description: 'some description',
      mbti: null,
      enneagram: null,
      variant: null,
      tritype: 0,
      socionics: null,
      sloan: null,
      psyche: null,
      image: null,
      is_active: true,
      created_at: entity.created_at,
    });
  });
});
