import { FileModel } from "./file.model";

export interface Appointment{
    id: number;
    timeSlot: Date;
    doctorTaj: string;
    patientTaj: string;
    description: string;
    status: string;
    files: FileModel[];
}