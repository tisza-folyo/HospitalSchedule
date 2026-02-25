import { BedModel } from "./bed.model";
import { NurseModel } from "./nurse/nurse.model";
import { PatientModel } from "./patient/patient.model";
import { RoomModel } from "./room.model";

export interface CareModel{
    patient: PatientModel;
    nurse: NurseModel;
    room: RoomModel;
    bed: BedModel;
    uTaj: string;
    entryDay: string;
    exitDay:string;
}