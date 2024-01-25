import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';

export type ProfileDocument = HydratedDocument<Profile>;

@Schema()
export class Profile {
  @Prop()
  profile_id: string;
  @Prop({ type: String, required: true })
  name: string;
  @Prop()
  description: string | null;
  @Prop()
  mbti: string | null;
  @Prop()
  enneagram: string | null;
  @Prop()
  variant: string | null;
  @Prop()
  tritype: number | 0;
  @Prop()
  socionics: string | null;
  @Prop()
  sloan: string | null;
  @Prop()
  psyche: string | null;
  @Prop()
  image: string | null;
  @Prop()
  is_active: boolean;
  @Prop({ type: Date, required: true })
  created_at: Date;
}

export const ProfileModel = SchemaFactory.createForClass(Profile);
