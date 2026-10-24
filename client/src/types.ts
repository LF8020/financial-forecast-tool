export interface Client {
    id?: number;
    name: string;
    email: string;
    phone: string;
    password: string;
}

export interface Asset {
    id: number;
    name: string;
    description: string;
    worth: number;
    apr: number;
    mortgage: number;
    interest: number;
}

export interface Scenario {
    id: number;
    name: string;
    description: string;
}