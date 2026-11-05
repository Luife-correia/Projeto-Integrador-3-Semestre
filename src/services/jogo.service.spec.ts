import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../environments/environments';
import { JogoService } from './jogo.service';

describe('JogoService', () => {
  let service: JogoService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(JogoService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTestingController.verify());

  it('should search by game name using the configured API URL', () => {
    service.listar(' Zelda ').subscribe((games) => expect(games).toEqual([]));

    const request = httpTestingController.expectOne(
      (request) =>
        request.url === `${environment.apiUrl}/jogos` && request.params.get('nome') === 'Zelda',
    );
    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('should request all games when no search term is provided', () => {
    service.listar().subscribe((games) => expect(games).toEqual([]));

    const request = httpTestingController.expectOne(`${environment.apiUrl}/jogos`);
    expect(request.request.params.has('nome')).toBe(false);
    request.flush([]);
  });
});
