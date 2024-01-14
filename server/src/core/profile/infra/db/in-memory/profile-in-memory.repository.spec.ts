import { Profile } from '../../../domain/profile.aggregate';
import { ProfileInMemoryRepository } from './profile-in-memory.repository';

describe('ProfileInMemoryRepository', () => {
  let repository: ProfileInMemoryRepository;

  beforeEach(() => (repository = new ProfileInMemoryRepository()));
  it('should no filter items when filter object is null', async () => {
    const items = [Profile.fake().aProfile().build()];
    const filterSpy = jest.spyOn(items, 'filter' as any);

    const itemsFiltered = await repository['applyFilter'](items, null);
    expect(filterSpy).not.toHaveBeenCalled();
    expect(itemsFiltered).toStrictEqual(items);
  });

  it('should filter items using filter parameter', async () => {
    const items = [
      Profile.fake().aProfile().withName('test').build(),
      Profile.fake().aProfile().withName('TEST').build(),
      Profile.fake().aProfile().withName('fake').build(),
    ];
    const filterSpy = jest.spyOn(items, 'filter' as any);

    const itemsFiltered = await repository['applyFilter'](items, 'TEST');
    expect(filterSpy).toHaveBeenCalledTimes(1);
    expect(itemsFiltered).toStrictEqual([items[0], items[1]]);
  });

  it('should sort by created_at when sort param is null', async () => {
    const created_at = new Date();

    const items = [
      Profile.fake()
        .aProfile()
        .withName('test')
        .withCreatedAt(created_at)
        .build(),
      Profile.fake()
        .aProfile()
        .withName('TEST')
        .withCreatedAt(new Date(created_at.getTime() + 100))
        .build(),
      Profile.fake()
        .aProfile()
        .withName('fake')
        .withCreatedAt(new Date(created_at.getTime() + 200))
        .build(),
    ];

    const itemsSorted = await repository['applySort'](items, null, null);
    expect(itemsSorted).toStrictEqual([items[2], items[1], items[0]]);
  });

  it('should sort by name', async () => {
    const items = [
      Profile.fake().aProfile().withName('c').build(),
      Profile.fake().aProfile().withName('b').build(),
      Profile.fake().aProfile().withName('a').build(),
    ];

    let itemsSorted = await repository['applySort'](items, 'name', 'asc');
    expect(itemsSorted).toStrictEqual([items[2], items[1], items[0]]);

    itemsSorted = await repository['applySort'](items, 'name', 'desc');
    expect(itemsSorted).toStrictEqual([items[0], items[1], items[2]]);
  });
});
