import { validateSync } from 'class-validator';
import { IValidatorFields } from './validator-fields-interface';
import logger from './../../../shared/infra/configs/logger.config';

export abstract class ClassValidatorFields implements IValidatorFields {
  validate(data: any, fields: string[]): boolean {
    const errors = validateSync(data, {
      groups: fields,
    });
    if (errors.length) {
      for (const error of errors) {
        const field = error.property;
        Object.values(error.constraints!).forEach((message) => {
          logger.error(message, field);
        });
      }
    }
    return !errors.length;
  }
}
