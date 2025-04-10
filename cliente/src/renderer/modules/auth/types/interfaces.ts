export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  name: string;
  email: string;
  password: string;
  ip?: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  ip?: string;
  photo?: string;
}
