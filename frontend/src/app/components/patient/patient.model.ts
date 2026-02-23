import { FileModel } from "../file.model";

export interface PatientModel {
    taj: number;
    firstName: string;
    lastName: string;
    age: number;
    email: string;
    personalDocImages: FileModel[];
}