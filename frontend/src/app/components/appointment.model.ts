import { FileModel } from "./file.model";

export interface AppointmentModel{
    appointmentId: number;
    timeSlot: Date;
    doctorTaj: string;
    patientTaj: string;
    description: string;
    status: string;
    files: FileModel[];
}