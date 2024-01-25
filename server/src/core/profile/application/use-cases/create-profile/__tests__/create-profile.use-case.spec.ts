import { ProfileInMemoryRepository } from '../../../../infra/db/in-memory/profile-in-memory.repository';
import { CreateProfileUseCase } from '../create-profile.use-case';

describe('CreateProfileUseCase Unit Tests', () => {
  let useCase: CreateProfileUseCase;
  let repository: ProfileInMemoryRepository;

  beforeEach(() => {
    repository = new ProfileInMemoryRepository();
    useCase = new CreateProfileUseCase(repository);
  });

  it('should throw an error when aggregate is not valid', async () => {
    const input = { name: 't'.repeat(256) };
    await expect(() => useCase.execute(input)).rejects.toThrowError(
      'Entity Validation Error',
    );
  });

  it('should create a profile', async () => {
    const spyInsert = jest.spyOn(repository, 'insert');
    let output = await useCase.execute({ name: 'test' });
    expect(spyInsert).toHaveBeenCalledTimes(1);
    expect(output).toStrictEqual({
      id: repository.items[0].profile_id.id,
      name: 'test',
      description: null,
      mbti: null,
      enneagram: null,
      variant: null,
      tritype: 0,
      socionics: null,
      sloan: null,
      psyche: null,
      image: null,
      is_active: true,
      created_at: repository.items[0].created_at,
    });

    output = await useCase.execute({
      name: 'test',
      description: 'some description',
      is_active: false,
    });
    expect(spyInsert).toHaveBeenCalledTimes(2);
    expect(output).toStrictEqual({
      id: repository.items[1].profile_id.id,
      name: 'test',
      description: 'some description',
      mbti: null,
      enneagram: null,
      variant: null,
      tritype: 0,
      socionics: null,
      sloan: null,
      psyche: null,
      image: null,
      is_active: false,
      created_at: repository.items[1].created_at,
    });
  });
});
