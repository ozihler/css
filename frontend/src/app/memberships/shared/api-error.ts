import { HttpErrorResponse } from '@angular/common/http';

interface ProblemDetails {
  readonly detail?: string;
  readonly title?: string;
}

export function apiErrorMessage(error: unknown, fallback: string): string {
  if (!(error instanceof HttpErrorResponse)) {
    return fallback;
  }

  const problem = error.error as ProblemDetails | null;
  return problem?.detail || problem?.title || fallback;
}
