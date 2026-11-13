import { HttpErrorResponse } from '@angular/common/http';
export function apiErrorMessage(
  error: unknown,
  fallback = 'Não foi possível concluir a operação. Tente novamente.',
): string {
  if (error instanceof HttpErrorResponse)
    return (
      error.error?.message ||
      (error.status === 0 ? 'Não foi possível conectar ao servidor.' : fallback)
    );
  return fallback;
}
