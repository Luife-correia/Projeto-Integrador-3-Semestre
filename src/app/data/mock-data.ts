import { Injectable, signal } from '@angular/core';

export type LibraryStatus = 'JOGANDO' | 'ZERADO' | 'ABANDONADO' | 'DESEJO_JOGAR' | 'PLATINADO';

export interface User {
  id: number;
  nome: string;
  email: string;
  fotoPerfil: string;
  bio: string;
}

export interface Game {
  id: string;
  nome: string;
  capa: string;
  plataformas: string[];
  generos: string[];
  desenvolvedora: string;
  dataLancamento: string;
  sinopse: string;
}

export interface LibraryEntry {
  usuarioId: number;
  jogoId: string;
  status: LibraryStatus;
  nota: number | null;
  horasJogadas: number;
}

export interface Review {
  usuarioId: number;
  jogoId: string;
  texto: string;
  nota: number;
  dataCriacao: string;
}

export const MOCK_USERS: User[] = [
  {
    id: 1,
    nome: 'Marina Costa',
    email: 'marina@organostation.dev',
    fotoPerfil: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=240&q=85',
    bio: 'Coleciono mundos que deixam saudade. No momento: revisitando clássicos de PS2.',
  },
  {
    id: 2,
    nome: 'Rafa Nascimento',
    email: 'rafa@organostation.dev',
    fotoPerfil: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=160&q=80',
    bio: 'A vida é curta demais para pular cutscenes.',
  },
  {
    id: 3,
    nome: 'Lia Martins',
    email: 'lia@organostation.dev',
    fotoPerfil: 'https://images.unsplash.com/photo-1531123897727-8f129e1688ce?auto=format&fit=crop&w=160&q=80',
    bio: 'RPGs longos e finais devastadores.',
  },
];

export const MOCK_GAMES: Game[] = [
  {
    id: 'shadow-of-the-colossus', nome: 'Shadow of the Colossus',
    capa: 'https://greenhillszone.com/wp-content/uploads/2012/04/shadow-of-the-colossus-box-art-pal.jpg',
    plataformas: ['PlayStation 2', 'PlayStation 3'], generos: ['Aventura', 'Ação'],
    desenvolvedora: 'Team Ico', dataLancamento: '2005-10-18',
    sinopse: 'Em uma terra esquecida, Wander atravessa planícies e ruínas para enfrentar dezesseis colossos. Cada batalha é um quebra-cabeça de escala monumental, em uma jornada silenciosa sobre perda, sacrifício e o preço de desafiar o destino.',
  },
  {
    id: 'god-of-war', nome: 'God of War',
    capa: 'https://greenhillszone.com/wp-content/uploads/2012/08/932295_76312_front.jpg',
    plataformas: ['PlayStation 2'], generos: ['Ação', 'Hack and Slash'],
    desenvolvedora: 'Santa Monica Studio', dataLancamento: '2005-03-22',
    sinopse: 'Kratos, um guerreiro espartano marcado pelo passado, recebe uma missão dos deuses do Olimpo. Sua busca pela Caixa de Pandora atravessa templos, monstros e uma Grécia mitológica brutal.',
  },
  {
    id: 'final-fantasy-x', nome: 'Final Fantasy X',
    capa: 'https://i.pinimg.com/736x/64/c1/4b/64c14bb6cc02bd4b16c10324dd32ba77.jpg',
    plataformas: ['PlayStation 2'], generos: ['RPG'], desenvolvedora: 'Square Enix',
    dataLancamento: '2001-07-19', sinopse: 'Tidus desperta em Spira, um mundo ameaçado por Sin. Ao lado da invocadora Yuna e seus guardiões, ele parte em uma peregrinação que vai mudar o destino de todos.',
  },
  {
    id: 'persona-4', nome: 'Persona 4',
    capa: 'https://m.media-amazon.com/images/I/91fc4SvMR9L._AC_SL1500_.jpg',
    plataformas: ['PlayStation 2'], generos: ['RPG', 'Mistério'], desenvolvedora: 'Atlus',
    dataLancamento: '2008-07-10', sinopse: 'Uma sequência de crimes misteriosos abala a pequena cidade de Inaba. Entre a rotina escolar e um mundo escondido na TV, um grupo de amigos procura a verdade.',
  },
  {
    id: 'resident-evil-4', nome: 'Resident Evil 4',
    capa: 'https://www.vgdb.com.br/gf/fotos/games/media_49769/resident-evil-4--49769.jpg',
    plataformas: ['PlayStation 2', 'GameCube'], generos: ['Survival horror', 'Ação'],
    desenvolvedora: 'Capcom', dataLancamento: '2005-10-25',
    sinopse: 'Leon S. Kennedy é enviado a uma vila remota na Europa para resgatar a filha do presidente. O que começa como uma missão de busca logo se transforma em uma luta desesperada pela sobrevivência.',
  },
  {
    id: 'kingdom-hearts-ii', nome: 'Kingdom Hearts II',
    capa: 'https://images.tcdn.com.br/img/img_prod/1087887/kingdom_hearts_2_ps2_midia_fisica_usado_1651_1_f20a613a65783af8f5c9d11f63c93029.jpg',
    plataformas: ['PlayStation 2'], generos: ['RPG', 'Aventura'], desenvolvedora: 'Square Enix',
    dataLancamento: '2005-12-22', sinopse: 'Sora, Donald e Pateta retornam para enfrentar a Organização XIII e a ameaça dos Heartless. Uma aventura por mundos inspirados em histórias inesquecíveis.',
  },
  {
    id: 'devil-may-cry-3', nome: 'Devil May Cry 3',
    capa: 'https://i.3djuegos.com/juegos/3716/devil_may_cry_3/fotos/ficha/devil_may_cry_3-1691940.jpg',
    plataformas: ['PlayStation 2'], generos: ['Ação', 'Hack and Slash'],
    desenvolvedora: 'Capcom', dataLancamento: '2005-02-17', sinopse: 'Antes de se tornar o caçador de demônios que conhecemos, Dante enfrenta o próprio irmão em uma guerra que coloca a humanidade em risco.',
  },
  {
    id: 'burnout-3-takedown', nome: 'Burnout 3: Takedown',
    capa: 'https://m.media-amazon.com/images/I/81xTZa6GBUL._AC_UF1000,1000_QL80_.jpg',
    plataformas: ['PlayStation 2', 'Xbox'], generos: ['Corrida'], desenvolvedora: 'Criterion Games',
    dataLancamento: '2004-09-07', sinopse: 'Velocidade máxima, tráfego intenso e colisões espetaculares. Em Burnout 3, vencer é tão importante quanto tirar rivais da pista.',
  },
  {
    id: 'prince-of-persia-sands-of-time', nome: 'Prince of Persia: The Sands of Time',
    capa: 'https://images-na.ssl-images-amazon.com/images/I/51K7SY7WRRL._AC_.jpg',
    plataformas: ['PlayStation 2', 'PC'], generos: ['Aventura', 'Plataforma'],
    desenvolvedora: 'Ubisoft', dataLancamento: '2003-11-18', sinopse: 'Uma adaga capaz de controlar o tempo transforma a conquista de um palácio em uma catástrofe. O Príncipe precisa reparar o erro e salvar o reino.',
  },
  {
    id: 'tekken-5', nome: 'Tekken 5',
    capa: 'https://tse2.mm.bing.net/th/id/OIP.WgE2rWhIsaYty2De9gNgmQHaKd?r=0&rs=1&pid=ImgDetMain&o=7&rm=3',
    plataformas: ['PlayStation 2'], generos: ['Luta'], desenvolvedora: 'Namco',
    dataLancamento: '2004-11-24', sinopse: 'O Torneio Rei do Punho de Ferro está de volta. Lutadores de todo o mundo encaram o conflito da família Mishima em combates intensos.',
  },
  {
    id: 'mortal-kombat-armageddon', nome: 'Mortal Kombat: Armageddon',
    capa: 'https://www.lukiegames.com/assets/images/PS2/ps2_mortal_kombat_armageddon_premium_p_dprtn5.jpg',
    plataformas: ['PlayStation 2'], generos: ['Luta'], desenvolvedora: 'Midway Games',
    dataLancamento: '2006-10-09', sinopse: 'Guerreiros de todos os reinos se preparam para uma batalha final. O destino do universo será decidido no confronto derradeiro.',
  },
  {
    id: 'dragon-quest-viii', nome: 'Dragon Quest VIII',
    capa: 'https://th.bing.com/th/id/R.55cd33ee16444e5ffde8a5d67ef7b769?rik=%2f%2f6lSwzWWtYgLg&riu=http%3a%2f%2fwww.mobygames.com%2fimages%2fcovers%2fl%2f272849-dragon-quest-viii-journey-of-the-cursed-king-playstation-2-front-cover.jpg&ehk=1anCys574LcjcFbGj7RDsRG4AiTfeF4hW5PqA8cgu0g%3d&risl=&pid=ImgRaw&r=0',
    plataformas: ['PlayStation 2'], generos: ['RPG'], desenvolvedora: 'Level-5',
    dataLancamento: '2004-11-27', sinopse: 'Um reino inteiro foi transformado por uma maldição. Com seus companheiros, o herói parte por um mundo vasto para encontrar o responsável e quebrar o feitiço.',
  },
];

const INITIAL_LIBRARY: LibraryEntry[] = [
  { usuarioId: 1, jogoId: 'shadow-of-the-colossus', status: 'ZERADO', nota: 5, horasJogadas: 14 },
  { usuarioId: 1, jogoId: 'final-fantasy-x', status: 'ZERADO', nota: 4.5, horasJogadas: 48 },
  { usuarioId: 1, jogoId: 'persona-4', status: 'JOGANDO', nota: 4, horasJogadas: 22 },
  { usuarioId: 1, jogoId: 'resident-evil-4', status: 'PLATINADO', nota: 5, horasJogadas: 31 },
  { usuarioId: 1, jogoId: 'kingdom-hearts-ii', status: 'DESEJO_JOGAR', nota: null, horasJogadas: 0 },
  { usuarioId: 1, jogoId: 'devil-may-cry-3', status: 'ABANDONADO', nota: 3, horasJogadas: 6 },
];

export const MOCK_REVIEWS: Review[] = [
  { usuarioId: 2, jogoId: 'shadow-of-the-colossus', texto: 'Poucos jogos entendem que o silêncio também pode contar uma história. Cada colosso deixa uma marca, e o final continua comigo muito depois dos créditos.', nota: 5, dataCriacao: '2026-05-18' },
  { usuarioId: 3, jogoId: 'shadow-of-the-colossus', texto: 'O mundo parece vazio até você perceber que esse vazio é justamente o que dá peso a cada passo. Uma experiência que não envelhece.', nota: 4.5, dataCriacao: '2026-04-29' },
  { usuarioId: 1, jogoId: 'final-fantasy-x', texto: 'Uma jornada generosa, personagens que crescem junto com a história e uma trilha que nunca sai da cabeça.', nota: 4.5, dataCriacao: '2026-06-02' },
  { usuarioId: 1, jogoId: 'resident-evil-4', texto: 'Ritmo impecável do começo ao fim. Voltar para a vila é como reencontrar velhos amigos, só que todos estão tentando te matar.', nota: 5, dataCriacao: '2026-03-11' },
  { usuarioId: 2, jogoId: 'persona-4', texto: 'O melhor tipo de mistério: aquele que você não quer resolver porque ainda quer passar mais um dia com esse grupo.', nota: 4, dataCriacao: '2026-05-02' },
];

@Injectable({ providedIn: 'root' })
export class MockDataService {
  readonly games = MOCK_GAMES;
  readonly users = MOCK_USERS;
  readonly reviews = MOCK_REVIEWS;
  readonly library = signal<LibraryEntry[]>(INITIAL_LIBRARY);

  gameById(id: string | null | undefined): Game {
    return this.games.find((game) => game.id === id) ?? this.games[0];
  }

  setLibraryStatus(gameId: string, status: LibraryStatus): void {
    this.library.update((entries) => {
      const existing = entries.find((entry) => entry.usuarioId === 1 && entry.jogoId === gameId);
      if (existing) {
        return entries.map((entry) => entry === existing ? { ...entry, status } : entry);
      }
      return [...entries, { usuarioId: 1, jogoId: gameId, status, nota: null, horasJogadas: 0 }];
    });
  }
}