import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  DestroyRef,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize, Subscription } from 'rxjs';

import { apiErrorMessage } from '../shared/api-error';
import { Membership, MembershipStatus } from '../shared/membership';
import { MembershipsApi } from '../shared/memberships-api';

const PAGE_SIZE = 10;

@Component({
  selector: 'app-membership-list',
  imports: [DatePipe],
  templateUrl: './membership-list.html',
  styleUrl: './membership-list.scss',
})
export class MembershipListComponent implements OnInit, OnChanges {
  private readonly api = inject(MembershipsApi);
  private readonly destroyRef = inject(DestroyRef);
  private loadSubscription?: Subscription;

  @Input() refreshToken = 0;

  protected readonly memberships = signal<readonly Membership[]>([]);
  protected readonly statusFilter = signal<MembershipStatus | ''>('');
  protected readonly page = signal(0);
  protected readonly totalElements = signal(0);
  protected readonly loading = signal(false);
  protected readonly actionMembershipId = signal<string | null>(null);
  protected readonly pauseMembershipId = signal<string | null>(null);
  protected readonly pauseDurationInDays = signal(30);
  protected readonly errorMessage = signal<string | null>(null);

  protected readonly pageSize = PAGE_SIZE;

  ngOnInit(): void {
    this.loadMemberships();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['refreshToken'] && !changes['refreshToken'].firstChange) {
      this.page.set(0);
      this.loadMemberships();
    }
  }

  protected totalPages(): number {
    return Math.max(1, Math.ceil(this.totalElements() / PAGE_SIZE));
  }

  protected resultStart(): number {
    return this.totalElements() === 0 ? 0 : this.page() * PAGE_SIZE + 1;
  }

  protected resultEnd(): number {
    return Math.min((this.page() + 1) * PAGE_SIZE, this.totalElements());
  }

  protected changeStatus(event: Event): void {
    const selectedStatus = (event.target as HTMLSelectElement).value as MembershipStatus | '';
    this.statusFilter.set(selectedStatus);
    this.page.set(0);
    this.loadMemberships();
  }

  protected previousPage(): void {
    if (this.page() === 0) {
      return;
    }
    this.page.update((page) => page - 1);
    this.loadMemberships();
  }

  protected nextPage(): void {
    if (this.page() + 1 >= this.totalPages()) {
      return;
    }
    this.page.update((page) => page + 1);
    this.loadMemberships();
  }

  protected beginPause(membershipId: string): void {
    this.errorMessage.set(null);
    this.pauseDurationInDays.set(30);
    this.pauseMembershipId.set(membershipId);
  }

  protected cancelPause(): void {
    this.pauseMembershipId.set(null);
  }

  protected changePauseDuration(event: Event): void {
    this.pauseDurationInDays.set(Number((event.target as HTMLSelectElement).value));
  }

  protected confirmPause(membership: Membership): void {
    this.actionMembershipId.set(membership.id);
    this.errorMessage.set(null);
    this.api
      .pause(membership.id, this.pauseDurationInDays())
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.actionMembershipId.set(null)),
      )
      .subscribe({
        next: () => {
          this.pauseMembershipId.set(null);
          this.loadMemberships();
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage.set(
            apiErrorMessage(error, `Could not pause ${membership.memberName}.`),
          );
        },
      });
  }

  protected resume(membership: Membership): void {
    this.actionMembershipId.set(membership.id);
    this.errorMessage.set(null);
    this.api
      .resume(membership.id)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.actionMembershipId.set(null)),
      )
      .subscribe({
        next: () => this.loadMemberships(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage.set(
            apiErrorMessage(error, `Could not resume ${membership.memberName}.`),
          );
        },
      });
  }

  protected retry(): void {
    this.loadMemberships();
  }

  private loadMemberships(): void {
    this.loadSubscription?.unsubscribe();
    this.loading.set(true);
    this.errorMessage.set(null);

    const status = this.statusFilter() || undefined;
    this.loadSubscription = this.api
      .list({ page: this.page(), size: PAGE_SIZE, status })
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.loading.set(false)),
      )
      .subscribe({
        next: (result) => {
          this.memberships.set(result.memberships);
          this.totalElements.set(result.totalElements);
        },
        error: (error: HttpErrorResponse) => {
          this.memberships.set([]);
          this.totalElements.set(0);
          this.errorMessage.set(apiErrorMessage(error, 'Memberships could not be loaded.'));
        },
      });
  }
}
