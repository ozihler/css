import { Component, signal } from '@angular/core';

import { MembershipListComponent } from './memberships/manage-memberships/membership-list';
import { MembershipRegistrationComponent } from './memberships/register-membership/membership-registration';

@Component({
  selector: 'app-root',
  imports: [MembershipListComponent, MembershipRegistrationComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly refreshToken = signal(0);

  protected refreshMemberships(): void {
    this.refreshToken.update((value) => value + 1);
  }
}
