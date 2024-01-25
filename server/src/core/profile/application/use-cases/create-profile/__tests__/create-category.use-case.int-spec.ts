import { setupMongoose } from '../../../../../shared/infra/testing/helpers';
import { ProfileId } from '../../../../domain/profile.aggregate';
import { ProfileMongooseRepository } from '../../../../infra/db/mongoose/profile-mongoose.repository';
import { ProfileModel } from '../../../../infra/db/mongoose/profile.model';
import { CreateProfileUseCase } from '../create-profile.use-case';

describe('CreateProfileUseCase Integration Tests', () => {
  let useCase: CreateProfileUseCase;
  let repository: ProfileMongooseRepository;

  setupMongoose({ models: [ProfileModel] });

  beforeEach(() => {
    repository = new ProfileMongooseRepository(ProfileModel);
    useCase = new CreateProfileUseCase(repository);
  });

  it('should create a profile', async () => {
    let output = await useCase.execute({ name: 'test' });
    let entity = await repository.findById(new ProfileId(output.id));
    expect(output).toStrictEqual({
      id: entity!.profile_id.id,
      name: 'test',
      description: null,
      is_active: true,
      created_at: entity!.created_at,
    });

    output = await useCase.execute({
      name: 'test',
      description: 'some description',
    });
    entity = await repository.findById(new ProfileId(output.id));
    expect(output).toStrictEqual({
      id: entity!.profile_id.id,
      name: 'test',
      description: 'some description',
      is_active: true,
      created_at: entity!.created_at,
    });

    output = await useCase.execute({
      name: 'test',
      description: 'some description',
      is_active: true,
    });
    entity = await repository.findById(new ProfileId(output.id));
    expect(output).toStrictEqual({
      id: entity!.profile_id.id,
      name: 'test',
      description: 'some description',
      is_active: true,
      created_at: entity!.created_at,
    });

    output = await useCase.execute({
      name: 'test',
      description: 'some description',
      is_active: false,
    });
    entity = await repository.findById(new ProfileId(output.id));
    expect(output).toStrictEqual({
      id: entity!.profile_id.id,
      name: 'test',
      description: 'some description',
      is_active: false,
      created_at: entity!.created_at,
    });
  });
});
