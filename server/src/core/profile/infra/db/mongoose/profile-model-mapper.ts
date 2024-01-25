import { LoadEntityError } from '../../../../shared/domain/validators/validation.error';
import { Profile, ProfileId } from '../../../domain/profile.aggregate';
import { ProfileModel } from './profile.model';

export class ProfileModelMapper {
  static toModel(entity: Profile): ProfileModel {
    return ProfileModel.build({
      profile_id: entity.profile_id.id,
      name: entity.name,
      description: entity.description,
      mbti: entity.mbti,
      enneagram: entity.enneagram,
      variant: entity.variant,
      tritype: entity.tritype,
      socionics: entity.socionics,
      sloan: entity.sloan,
      psyche: entity.psyche,
      image: entity.image,
      is_active: entity.is_active,
      created_at: entity.created_at,
    });
  }

  static toEntity(model: ProfileModel): Profile {
    const profile = new Profile({
      profile_id: new ProfileId(model.profile_id),
      name: model.name,
      description: model.description,
      mbti: model.mbti,
      enneagram: model.enneagram,
      variant: model.variant,
      tritype: model.tritype,
      socionics: model.socionics,
      sloan: model.sloan,
      psyche: model.psyche,
      image: model.image,
      is_active: model.is_active,
      created_at: model.created_at,
    });

    profile.validate();
    if (profile.notification.hasErrors()) {
      throw new LoadEntityError(profile.notification.toJSON());
    }
    return profile;
  }
}
