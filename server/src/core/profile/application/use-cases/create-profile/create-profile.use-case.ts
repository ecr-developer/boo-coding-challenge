import { IUseCase } from '../../../../shared/application/use-case.interface';
import { EntityValidationError } from '../../../../shared/domain/validators/validation.error';
import { Profile } from '../../../domain/profile.aggregate';
import { IProfileRepository } from '../../../domain/profile.repository';
import { ProfileOutput, ProfileOutputMapper } from '../common/profile-output';
import { CreateProfileInput } from './create-profile.input';

export class CreateProfileUseCase
  implements IUseCase<CreateProfileInput, CreateProfileOutput>
{
  constructor(private readonly profileRepo: IProfileRepository) {}

  async execute(input: CreateProfileInput): Promise<CreateProfileOutput> {
    const entity = Profile.create(input);

    if (entity.notification.hasErrors()) {
      throw new EntityValidationError(entity.notification.toJSON());
    }

    await this.profileRepo.insert(entity);

    return ProfileOutputMapper.toOutput(entity);
  }
}

export type CreateProfileOutput = ProfileOutput;
