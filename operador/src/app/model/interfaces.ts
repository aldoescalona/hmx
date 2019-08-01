export interface Imagen {
    id: any;
    imgA: string;
    imgB: string;
    imgC: string;
}

export interface Oferta {
    baja: boolean;
    calificacion: number;
    calificacion1: number;
    calificacion2: number;
    calificacion3: number;
    calificacion4: number;
    calificacion5: number;
    calificaciones: number;
    condiciones: string;
    descripcion: string;
    estado: boolean;
    id: number;
    imagenId: Imagen;
    nombre: string;
    orden: number;
    precio: number;
    tipo: number;
    vendidos: number;
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

export interface Compra {
    estado: string;
    fecha: Date;
    folio: number;
    id: number;
    ofertaId: Oferta;
    puntos: number;
    usuarioId: Usuario;
    fechaRespuesta?: Date;
    respuesta: string;
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
    nombre: string;
    orden: number;
    puntosCanjeados: number;
    puntosCanjeadosOtros: number;
    puntosGenerados: number;
    sitioWeb: string;
    telefono: string;
    tipo: string;
}


export interface Visita {
    calificacion: number;
    estado: string;
    fecha: Date;
    fechaRespuesta: Date;
    id: number;
    monto: number;
    resena: string;
    respuesta: string;
    ticket: string;
    usuarioId: Usuario;
    sel: boolean;
}

export interface DashboardOperador {
    compras: Compra[];
    lugar: Lugar;
    ok: boolean;
    visitas: Visita[];
}

export interface RespuestaSimple{
    ok: boolean;
    codigo: number;
    mensaje: string
}