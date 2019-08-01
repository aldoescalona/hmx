export interface Imagen {
    id: any;
    imgA: string;
    imgB: string;
    imgC: string;
}

export interface Oferta {
    baja?: boolean;
    calificacion?: number;
    calificacion1?: number;
    calificacion2?: number;
    calificacion3?: number;
    calificacion4?: number;
    calificacion5?: number;
    calificaciones?: number;
    condiciones: string;
    descripcion: string;
    estado?: boolean;
    id?: number;
    imagenId?: Imagen;
    nombre: string;
    orden?: number;
    precio?: number;
    tipo?: string;
    vendidos?: number;
    lugarId?: Lugar;
}

export interface Evento {
    id?: number;
    titulo: string;
    fechaInicio?: Date;
    fechaFin?: Date;
    estado?: boolean;
    tipo?: number;
    imagenId?: Imagen;
    lugarId?: Lugar;
}

export interface Usuario {
    email: string;
    estado: string;
    id: number;
    imagenURL: string;
    nombre: string;
    puntos: number;
    retenidos: number;
    sexo: string;
    telefono: string;
    facebookid: string;
}

export interface Lugar {
    alias: string;
    calificacion: number;
    calificacion1: number;
    calificacion2: number;
    calificacion3: number;
    calificacion4: number;
    calificacion5: number;
    calificaciones: number;
    correo: string;
    descripcion: string;
    direccion: string;
    estado: string;
    id: number;
    imagenLogoId: Imagen;
    imagenPortadaId: Imagen;
    nombre: string;
    orden: number;
    puntosCanjeados: number;
    puntosCanjeadosOtros: number;
    puntosGenerados: number;
    sitioWeb: string;
    telefono: string;
    tipo: string;
    ubicacionUrl: string;
    ubicacionLng: string;
    ubicacionLtd: string;
    precioMin: number;
    precioMax: number;
}

export interface Operador {
    id?: number;
    nombre: string;
    clave: string;
    contrasena?: any;
    estado?: boolean;
    lugarId?: Lugar;
}

export interface RespuestaSimple{
    ok: boolean;
    codigo: number;
    mensaje: string;
}

export interface CUDResponse{
    id: number;
    ok: boolean;
    tipo: string;
    codigo: number;
    mensaje: string;
}

export interface Cliente {
    correo: string;
    estado: string;
    id: number;
    nombre: string;
}

export interface MapaCliente{
    cliente: Cliente;
}