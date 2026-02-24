import { FileModel } from "../file.model";

export interface PatientModel {
    taj: string;
    firstName: string;
    lastName: string;
    age: number;
    email: string;
    personalDocImages: FileModel[];
}