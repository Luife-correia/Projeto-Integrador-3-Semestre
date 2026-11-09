import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MockDataService, type User } from '../../data/mock-data';

type Modo = 'login' | 'cadastro';

@Component({
  selector: 'app-login-cadastro',
  standalone: true,
  imports: [FormsModule, RouterLink],
  styleUrl: './login-cadastro.css',
  templateUrl: './login-cadastro.html',
})

export class LoginCadastro {
  private readonly mockData = inject(MockDataService);

  private readonly usuarios: User[] = [...this.mockData.users];

  // Senha fictícia para os usuários preexistentes no mock.
  private readonly senhasMock = new Map<string, string>(
    this.usuarios.map((usuario) => [
      this.normalizarEmail(usuario.email),
      'gamebox123',
    ]),
  );

  modo: Modo = 'login';

  dados = {
    nome: '',
    email: '',
    senha: '',
    confirmacaoSenha: '',
  };

  mensagem = '';
  tipoMensagem: 'erro' | 'sucesso' | '' = '';
  usuarioAtual: User | null = null;

  enviarFormulario(formulario: NgForm): void {
    formulario.control.markAllAsTouched();

    if (formulario.invalid) {
      return;
    }

    this.mensagem = '';
    this.tipoMensagem = '';

    if (this.modo === 'cadastro') {
      this.cadastrar(formulario);
      return;
    }

    this.entrar();
  }

  alternarModo(novoModo: Modo, formulario: NgForm): void {
    this.modo = novoModo;
    this.dados = {
      nome: '',
      email: '',
      senha: '',
      confirmacaoSenha: '',
    };
    this.mensagem = '';
    this.tipoMensagem = '';
    this.usuarioAtual = null;

    formulario.resetForm(this.dados);
  }

  private cadastrar(formulario: NgForm): void {
    if (this.dados.senha !== this.dados.confirmacaoSenha) {
      this.mensagem = 'As senhas preciam corresponder';
      this.tipoMensagem = 'erro';
      return;
    }

    const email = this.normalizarEmail(this.dados.email);
    const emailEmUso = this.usuarios.some(
      (usuario) => this.normalizarEmail(usuario.email) === email,
    );

    if (emailEmUso) {
      this.mensagem = 'Já existe uma conta com este e-mail.';
      this.tipoMensagem = 'erro';
      return;
    }

    const novoUsuario: User = {
      id: Math.max(0, ...this.usuarios.map((usuario) => usuario.id)) + 1,
      nome: this.dados.nome.trim(),
      email,
      fotoPerfil: '',
      bio: '',
      role: 'USER',
      ativo: true,
    };

    this.usuarios.push(novoUsuario);

    this.senhasMock.set(email, this.dados.senha);

    this.modo = 'login';
    this.dados = {
      nome: '',
      email,
      senha: '',
      confirmacaoSenha: '',
    };
    formulario.resetForm(this.dados);

    this.mensagem = 'Conta criada! Agora você já pode entrar.';
    this.tipoMensagem = 'sucesso';
  }

  private entrar(): void {
    const email = this.normalizarEmail(this.dados.email);
    const usuario = this.usuarios.find(
      (item) => this.normalizarEmail(item.email) === email,
    );
    const senhaCorreta = this.senhasMock.get(email) === this.dados.senha;

    if (!usuario || !senhaCorreta) {
      this.usuarioAtual = null;
      this.mensagem = 'E-mail ou senha inválidos.';
      this.tipoMensagem = 'erro';
      return;
    }

    this.usuarioAtual = usuario;
    this.mensagem = `Boas-vindas, ${usuario.nome}!`;
    this.tipoMensagem = 'sucesso';
  }

  private normalizarEmail(email: string): string {
    return email.trim().toLowerCase();
  }
}