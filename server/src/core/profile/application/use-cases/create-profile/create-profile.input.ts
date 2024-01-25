import {
  IsBoolean,
  IsNotEmpty,
  IsOptional,
  IsString,
  IsNumber,
  validateSync,
} from 'class-validator';

export type CreateProfileInputConstructorProps = {
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
};

export class CreateProfileInput {
  @IsString()
  @IsNotEmpty()
  name: string;

  @IsString()
  @IsOptional()
  description?: string | null;

  @IsString()
  @IsOptional()
  mbti?: string | null;

  @IsString()
  @IsOptional()
  enneagram?: string | null;

  @IsString()
  @IsOptional()
  variant?: string | null;

  @IsNumber()
  @IsOptional()
  tritype?: number | 0;

  @IsString()
  @IsOptional()
  socionics?: string | null;

  @IsString()
  @IsOptional()
  sloan?: string | null;

  @IsString()
  @IsOptional()
  psyche?: string | null;

  @IsString()
  @IsOptional()
  image?: string | null;

  @IsBoolean()
  @IsOptional()
  is_active?: boolean;

  constructor(props: CreateProfileInputConstructorProps) {
    if (!props) return;
    this.name = props.name;
    this.description = props.description;
    this.mbti = props.mbti;
    this.enneagram = props.enneagram;
    this.variant = props.variant;
    this.tritype = props.tritype;
    this.socionics = props.socionics;
    this.sloan = props.sloan;
    this.psyche = props.psyche;
    this.image = props.image;
    this.is_active = props.is_active;
  }
}

export class ValidateCreateProfileInput {
  static validate(input: CreateProfileInput) {
    return validateSync(input);
  }
}
