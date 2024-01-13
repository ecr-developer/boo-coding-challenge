import { AggregateRoot } from '../../shared/domain/aggregate-root';
import { ValueObject } from '../../shared/domain/value-object';
import { Uuid } from '../../shared/domain/value-objects/uuid.vo';
import { ProfileValidatorFactory } from './profile.validator';

export class ProfileId extends Uuid {}

export type ProfileConstructorProps = {
  profile_id?: ProfileId;
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
  is_active?: boolean;
  created_at?: Date;
};

export type ProfileCreateCommand = {
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
  is_active?: boolean;
};

export class Profile extends AggregateRoot {
  profile_id: ProfileId;
  name: string;
  description: string | null;
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

  constructor(props: ProfileConstructorProps) {
    super();
    this.profile_id = props.profile_id ?? new ProfileId();
    this.name = props.name;
    this.description = props.description ?? null;
    this.mbti = props.mbti ?? null;
    this.enneagram = props.enneagram ?? null;
    this.variant = props.variant ?? null;
    this.tritype = props.tritype ?? 0;
    this.socionics = props.socionics ?? null;
    this.sloan = props.sloan ?? null;
    this.psyche = props.psyche ?? null;
    this.image = props.image ?? null;
    this.is_active = props.is_active ?? true;
    this.created_at = props.created_at ?? new Date();
  }

  get entity_id(): ValueObject {
    return this.profile_id;
  }

  static create(props: ProfileCreateCommand): Profile {
    const profile = new Profile(props);
    profile.validate(['name']);
    return profile;
  }

  activate() {
    this.is_active = true;
  }

  deactivate() {
    this.is_active = false;
  }

  validate(fields?: string[]) {
    const validator = ProfileValidatorFactory.create();
    return validator.validate(this.notification, this, fields);
  }

  toJSON() {
    return {
      id: this.profile_id.id,
      name: this.name,
      description: this.description,
      mbti: this.mbti,
      enneagram: this.enneagram,
      variant: this.variant,
      tritype: this.tritype,
      socionics: this.socionics,
      sloan: this.sloan,
      psyche: this.psyche,
      image: this.image,
      is_active: this.is_active,
      created_at: this.created_at,
    };
  }
}
