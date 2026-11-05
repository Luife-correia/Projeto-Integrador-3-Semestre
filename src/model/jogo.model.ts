export interface Jogo {
 id: number;
 nome: string;
 capa: string;
 plataformas: string[]; // List<String> no Java
 generos: string[];
 desenvolvedora: string;
 dataLancamento: string; // LocalDate chega como texto "2017-03-03"
 sinopse: string;
 notaTotal: number; // double no Java
}
