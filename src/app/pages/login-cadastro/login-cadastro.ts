import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';
import { AuthService } from '../../../services/auth.service';
import { apiErrorMessage } from '../../../services/api-error';
type Modo = 'login' | 'cadastro';
@Component({
  selector: 'app-login-cadastro',
  standalone: true,
  imports: [FormsModule, RouterLink],
  styleUrl: './login-cadastro.css',
  templateUrl: './login-cadastro.html',
})
export class LoginCadastro {
  private api = inject(UsuarioService);
  private auth = inject(AuthService);
  private router = inject(Router);
  // Guarda o modo atual, os campos do formulário e as mensagens mostradas na tela.
  modo: Modo = 'login';
  dados = { nome: '', email: '', senha: '', confirmacaoSenha: '' };
  mensagem = '';
  tipoMensagem: 'erro' | 'sucesso' | '' = '';
  usuarioAtual = null as { id: number; nome: string; email: string } | null;
  loading = false;

  // Valida o formulário e chama POST /usuarios ou POST /usuarios/login.
  enviarFormulario(form: NgForm) {
    form.control.markAllAsTouched();
    if (form.invalid || this.loading) return;
    this.mensagem = '';
    if (this.modo === 'cadastro') {
      if (this.dados.senha !== this.dados.confirmacaoSenha) {
        this.mensagem = 'As senhas precisam corresponder.';
        this.tipoMensagem = 'erro';
        return;
      }
      this.loading = true;
      this.api
        .cadastrar({
          nome: this.dados.nome.trim(),
          email: this.dados.email.trim(),
          senha: this.dados.senha,
        })
        .subscribe({
          next: () => {
            this.loading = false;
            this.modo = 'login';
            this.dados = { ...this.dados, senha: '', confirmacaoSenha: '' };
            this.mensagem = 'Conta criada. Agora entre com suas credenciais.';
            this.tipoMensagem = 'sucesso';
          },
          error: (erro) => {
            this.loading = false;
            this.mensagem = apiErrorMessage(erro);
            this.tipoMensagem = 'erro';
          },
        });
      return;
    }
    this.loading = true;
    this.api.login({ email: this.dados.email.trim(), senha: this.dados.senha }).subscribe({
      next: (usuarioLogado) => {
        this.loading = false;
        this.auth.login(usuarioLogado);
        this.usuarioAtual = usuarioLogado;
        this.mensagem = `Boas-vindas, ${usuarioLogado.nome}!`;
        this.tipoMensagem = 'sucesso';
        this.router.navigateByUrl('/home');
      },
      error: (erro) => {
        this.loading = false;
        this.mensagem = apiErrorMessage(erro, 'E-mail ou senha inválidos.');
        this.tipoMensagem = 'erro';
      },
    });
  }
  // Alterna entre login e cadastro e limpa o formulário.
  alternarModo(novoModo: Modo, form: NgForm) {
    this.modo = novoModo;
    this.dados = { nome: '', email: '', senha: '', confirmacaoSenha: '' };
    this.mensagem = '';
    this.tipoMensagem = '';
    form.resetForm(this.dados);
  }
}
