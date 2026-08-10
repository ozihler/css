import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import { apiErrorMessage } from '../shared/api-error';
import { Membership, PlanCode } from '../shared/membership';
import { MembershipsApi } from '../shared/memberships-api';

@Component({
  selector: 'app-membership-registration',
  imports: [ReactiveFormsModule],
  templateUrl: './membership-registration.html',
  styleUrl: './membership-registration.scss',
})
export class MembershipRegistrationComponent {
  private readonly api = inject(MembershipsApi);
  private readonly formBuilder = inject(FormBuilder);

  readonly membershipRegistered = output<Membership>();

  protected readonly submitting = signal(false);
  protected readonly successMessage = signal<string | null>(null);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly form = this.formBuilder.nonNullable.group({
    memberName: ['', [Validators.required, Validators.maxLength(200)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(320)]],
    planCode: this.formBuilder.nonNullable.control<PlanCode>('STANDARD'),
  });

  protected register(): void {
    this.successMessage.set(null);
    this.errorMessage.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.api
      .register(this.form.getRawValue())
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: (membership) => {
          this.successMessage.set(`${membership.memberName} is now registered.`);
          this.form.reset({ memberName: '', email: '', planCode: 'STANDARD' });
          this.membershipRegistered.emit(membership);
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage.set(apiErrorMessage(error, 'The membership could not be registered.'));
        },
      });
  }
}
