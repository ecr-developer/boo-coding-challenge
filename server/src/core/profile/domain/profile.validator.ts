import { MaxLength } from 'class-validator';
import { Profile } from './profile.aggregate';
import { ClassValidatorFields } from '../../shared/domain/validators/class-validator-fields';
import { Notification } from '../../shared/domain/validators/notification';

//create a test that checks the decorators
export class ProfileRules {
  @MaxLength(255, { groups: ['name'] })
  name: string;

  constructor(entity: Profile) {
    Object.assign(this, entity);
  }
}

export class ProfileValidator extends ClassValidatorFields {
  validate(notification: Notification, data: any, fields?: string[]): boolean {
    const newFields = fields?.length ? fields : ['name'];
    return super.validate(notification, new ProfileRules(data), newFields);
  }
}

export class ProfileValidatorFactory {
  static create() {
    return new ProfileValidator();
  }
}
