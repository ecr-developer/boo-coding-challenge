import { AggregateRoot } from '../../shared/domain/aggregate-root';
import { ValueObject } from '../../shared/domain/value-object';
import { Uuid } from '../../shared/domain/value-objects/uuid.vo';

export class ProfileId extends Uuid {}

export type ProfileConstructorProps = {
  profile_id?: ProfileId;
  name: string;
  description?: string | null;
  mbti: string;
  enneagram: string;
  variant: string;
  tritype: number;
  socionics: string;
  sloan: string;
  psyche: string;
  image: string;
  is_active?: boolean;
  created_at?: Date;
};

export type ProfileCreateCommand = {
  name: string;
  description?: string | null;
  mbti: string;
  enneagram: string;
  variant: string;
  tritype: number;
  socionics: string;
  sloan: string;
  psyche: string;
  image: string;
  is_active?: boolean;
};

export class Profile extends AggregateRoot {
  profile_id: ProfileId;
  name: string;
  description: string | null;
  mbti: string;
  enneagram: string;
  variant: string;
  tritype: number;
  socionics: string;
  sloan: string;
  psyche: string;
  image: string;
  is_active: boolean;
  created_at: Date;

  constructor(props: ProfileConstructorProps) {
    super();
    this.profile_id = props.profile_id ?? new ProfileId();
    this.name = props.name;
    this.description = props.description ?? null;
    this.mbti = props.mbti;
    this.enneagram = props.enneagram;
    this.variant = props.variant;
    this.tritype = props.tritype;
    this.socionics = props.socionics;
    this.sloan = props.sloan;
    this.psyche = props.psyche;
    this.image = props.image;
    this.is_active = props.is_active ?? true;
    this.created_at = props.created_at ?? new Date();
  }

  get entity_id(): ValueObject {
    return this.profile_id;
  }

  static create(props: ProfileCreateCommand): Profile {
    const profile = new Profile(props);
    return profile;
  }

  activate() {
    this.is_active = true;
  }

  deactivate() {
    this.is_active = false;
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
