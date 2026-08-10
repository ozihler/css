import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Membership, MembershipPage } from '../shared/membership';
import { MembershipListComponent } from './membership-list';

const ACTIVE_MEMBERSHIP: Membership = {
  id: '506241e8-b395-4a7e-8af7-ca8fbaa5f1d2',
  memberName: 'Maya Chen',
  email: 'maya@example.com',
  planCode: 'PREMIUM',
  status: 'ACTIVE',
  activatedOn: '2026-08-10',
  pausedFrom: null,
  resumeOn: null,
};

const PAUSED_MEMBERSHIP: Membership = {
  ...ACTIVE_MEMBERSHIP,
  status: 'PAUSED',
  pausedFrom: '2026-08-10',
  resumeOn: '2026-09-24',
};

describe('Membership directory', () => {
  let fixture: ComponentFixture<MembershipListComponent>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembershipListComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(MembershipListComponent);
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => http.verify());

  it('shows memberships returned by the admin query', () => {
    expectAdminQuery({ page: '0', size: '10' }).flush(pageOf(ACTIVE_MEMBERSHIP));
    fixture.detectChanges();

    expect(pageText()).toContain('Maya Chen');
    expect(pageText()).toContain('maya@example.com');
    expect(pageText()).toContain('Premium');
    expect(buttonNamed('Pause Maya Chen')).toBeTruthy();
  });

  it('queries paused memberships when the operator changes the status filter', () => {
    expectAdminQuery({ page: '0', size: '10' }).flush(pageOf(ACTIVE_MEMBERSHIP));
    fixture.detectChanges();

    chooseStatus('PAUSED');
    expectAdminQuery({ page: '0', size: '10', status: 'PAUSED' }).flush(pageOf(PAUSED_MEMBERSHIP));
    fixture.detectChanges();

    expect(pageText()).toContain('Paused');
    expect(buttonNamed('Resume Maya Chen')).toBeTruthy();
  });

  it('pauses an active membership for the duration selected by the operator', () => {
    expectAdminQuery({ page: '0', size: '10' }).flush(pageOf(ACTIVE_MEMBERSHIP));
    fixture.detectChanges();

    buttonNamed('Pause Maya Chen').click();
    fixture.detectChanges();
    choosePauseDuration('45');
    buttonNamed('Confirm pause').click();

    const pauseRequest = http.expectOne(`/api/memberships/${ACTIVE_MEMBERSHIP.id}/pause`);
    expect(pauseRequest.request.method).toBe('POST');
    expect(pauseRequest.request.body).toEqual({ durationInDays: 45 });
    pauseRequest.flush(PAUSED_MEMBERSHIP);
    expectAdminQuery({ page: '0', size: '10' }).flush(pageOf(PAUSED_MEMBERSHIP));
    fixture.detectChanges();

    expect(buttonNamed('Resume Maya Chen')).toBeTruthy();
  });

  it('resumes a paused membership and reloads its observable status', () => {
    expectAdminQuery({ page: '0', size: '10' }).flush(pageOf(PAUSED_MEMBERSHIP));
    fixture.detectChanges();

    buttonNamed('Resume Maya Chen').click();
    const resumeRequest = http.expectOne(`/api/memberships/${ACTIVE_MEMBERSHIP.id}/resume`);
    expect(resumeRequest.request.method).toBe('POST');
    resumeRequest.flush(ACTIVE_MEMBERSHIP);
    expectAdminQuery({ page: '0', size: '10' }).flush(pageOf(ACTIVE_MEMBERSHIP));
    fixture.detectChanges();

    expect(buttonNamed('Pause Maya Chen')).toBeTruthy();
  });

  function expectAdminQuery(expectedParams: Record<string, string>) {
    return http.expectOne((request) => {
      if (request.url !== '/api/admin/memberships' || request.method !== 'GET') {
        return false;
      }
      return Object.entries(expectedParams).every(
        ([name, value]) => request.params.get(name) === value,
      );
    });
  }

  function pageOf(membership: Membership): MembershipPage {
    return {
      page: 0,
      size: 10,
      totalElements: 1,
      memberships: [membership],
    };
  }

  function chooseStatus(status: 'ACTIVE' | 'PAUSED'): void {
    const select = controlLabelledBy('Status');
    select.value = status;
    select.dispatchEvent(new Event('change'));
    fixture.detectChanges();
  }

  function choosePauseDuration(days: string): void {
    const select = controlLabelledBy('Pause Maya Chen for');
    select.value = days;
    select.dispatchEvent(new Event('change'));
    fixture.detectChanges();
  }

  function controlLabelledBy(text: string): HTMLSelectElement {
    const labels = Array.from(
      fixture.nativeElement.querySelectorAll('label'),
    ) as HTMLLabelElement[];
    const label = labels.find((candidate) => candidate.textContent?.includes(text));
    const control = label?.htmlFor
      ? fixture.nativeElement.querySelector(`#${label.htmlFor}`)
      : label?.querySelector('select');
    if (!control) {
      throw new Error(`No control labelled "${text}" was found.`);
    }
    return control;
  }

  function buttonNamed(accessibleName: string): HTMLButtonElement {
    const buttons = Array.from(
      fixture.nativeElement.querySelectorAll('button'),
    ) as HTMLButtonElement[];
    const button = buttons.find(
      (candidate) =>
        candidate.getAttribute('aria-label') === accessibleName ||
        candidate.textContent?.includes(accessibleName),
    );
    if (!button) {
      throw new Error(`No button named "${accessibleName}" was found.`);
    }
    return button;
  }

  function pageText(): string {
    return fixture.nativeElement.textContent;
  }
});
