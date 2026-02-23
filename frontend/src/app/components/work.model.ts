import { AssistantModel } from "./assistant/assistant.model";
import { DoctorModel } from "./doctor/doctor.model";


export interface WorkModel {
    workId: number | null;
    workDay: string;
    uTaj: string;
    doctor: DoctorModel | null;
    assistants: AssistantModel | null;
}