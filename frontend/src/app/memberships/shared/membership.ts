export type MembershipStatus = 'ACTIVE' | 'PAUSED';
export type PlanCode = 'STANDARD' | 'PREMIUM';

export interface Membership {
  readonly id: string;
  readonly memberName: string;
  readonly email: string;
  readonly planCode: PlanCode;
  readonly status: MembershipStatus;
  readonly activatedOn: string;
  readonly pausedFrom: string | null;
  readonly resumeOn: string | null;
}

export interface MembershipPage {
  readonly page: number;
  readonly size: number;
  readonly totalElements: number;
  readonly memberships: readonly Membership[];
}

export interface RegisterMembership {
  readonly memberName: string;
  readonly email: string;
  readonly planCode: PlanCode;
}

export interface MembershipSearch {
  readonly page: number;
  readonly size: number;
  readonly status?: MembershipStatus;
}
