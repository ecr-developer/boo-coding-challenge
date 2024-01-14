import { Profile } from '../../../domain/profile.aggregate';

export type ProfileOutput = {
  id: string;
  name: string;
  description?: string | null;
  mbti?: string | null;
  enneagram?: string | null;
  variant?: string | null;
  tritype?: number | 0;
  socionics?: string | null;
  sloan?: string | null;
  psyche?: string | null;
  image?: string | null;
  is_active: boolean;
  created_at: Date;
};

export class ProfileOutputMapper {
  static toOutput(entity: Profile): ProfileOutput {
    const { profile_id, ...otherProps } = entity.toJSON();
    return {
      id: profile_id,
      ...otherProps,
    };
  }
}
