import {Component, inject, numberAttribute, output, signal} from '@angular/core';
import { email, FormField, form, FormRoot, maxLength, required } from '@angular/forms/signals';
import type { FieldTree, TreeValidationResult } from '@angular/forms/signals';
import { firstValueFrom } from 'rxjs';

import { apiErrorMessage } from '../shared/api-error';
import { Membership, RegisterMembership } from '../shared/membership';
import { MembershipsApi } from '../shared/memberships-api';

const EMPTY_REGISTRATION: RegisterMembership = {
  memberName: '',
  email: '',
  planCode: 'STANDARD',
};

@Component({
  standalone: true,
  selector: 'app-membership-registration',
  imports: [FormField, FormRoot],
  templateUrl: './membership-registration.html',
  styleUrl: './membership-registration.scss',
})
export class MembershipRegistrationComponent {
  private readonly api = inject(MembershipsApi);
  private readonly registrationModel = signal<RegisterMembership>(EMPTY_REGISTRATION);

  readonly membershipRegistered = output<Membership>();

  protected readonly successMessage = signal<string | null>(null);
  protected readonly registrationForm = form(
    this.registrationModel,
    (membership) =>
    {
      required(membership.memberName, { message: "Enter the member's name." });
      maxLength(membership.memberName, 200, {
        message: 'The member name cannot exceed 200 characters.',
      });

      required(membership.email, { message: 'Enter a valid email address.' });
      email(membership.email, { message: 'Enter a valid email address.' });
      maxLength(membership.email, 320, {
        message: 'The email address cannot exceed 320 characters.',
      });
    },
    {
      name: 'membership-registration',
      submission: {
        action: (registration) => this.registerMembership(registration),
      },
    },
  );

  private async registerMembership(
    registration: FieldTree<RegisterMembership>,
  ): Promise<TreeValidationResult> {
    this.successMessage.set(null);

    try {
      const membership = await firstValueFrom(this.api.register(registration().value()));
      this.successMessage.set(`${membership.memberName} is now registered.`);
      registration().reset(EMPTY_REGISTRATION);
      this.membershipRegistered.emit(membership);
      return undefined;
    } catch (error: unknown) {
      return {
        kind: 'server',
        message: apiErrorMessage(error, 'The membership could not be registered.'),
      };
    }
  }
}
