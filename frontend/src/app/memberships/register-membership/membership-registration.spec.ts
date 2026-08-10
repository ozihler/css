import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Membership } from '../shared/membership';
import { MembershipRegistrationComponent } from './membership-registration';

const REGISTERED_MEMBERSHIP: Membership = {
  id: '506241e8-b395-4a7e-8af7-ca8fbaa5f1d2',
  memberName: 'Maya Chen',
  email: 'maya@example.com',
  planCode: 'PREMIUM',
  status: 'ACTIVE',
  activatedOn: '2026-08-10',
  pausedFrom: null,
  resumeOn: null,
};

describe('Membership registration', () => {
  let fixture: ComponentFixture<MembershipRegistrationComponent>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembershipRegistrationComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(MembershipRegistrationComponent);
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => http.verify());

  it('shows helpful validation when required member details are missing', () => {
    buttonNamed('Register membership').click();
    fixture.detectChanges();

    expect(pageText()).toContain("Enter the member's name.");
    expect(pageText()).toContain('Enter a valid email address.');
  });

  it('registers a membership and confirms the outcome to the operator', () => {
    enterValue('Full name', 'Maya Chen');
    enterValue('Email address', 'maya@example.com');
    choosePlan('PREMIUM');

    buttonNamed('Register membership').click();

    const request = http.expectOne('/api/memberships');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      memberName: 'Maya Chen',
      email: 'maya@example.com',
      planCode: 'PREMIUM',
    });
    request.flush(REGISTERED_MEMBERSHIP);
    fixture.detectChanges();

    expect(pageText()).toContain('Maya Chen is now registered.');
  });

  it('shows the API problem detail when an email is already registered', () => {
    enterValue('Full name', 'Maya Chen');
    enterValue('Email address', 'maya@example.com');

    buttonNamed('Register membership').click();
    http.expectOne('/api/memberships').flush(
      {
        title: 'Duplicate email',
        detail: 'Email address maya@example.com is already registered.',
      },
      { status: 409, statusText: 'Conflict' },
    );
    fixture.detectChanges();

    expect(pageText()).toContain('Email address maya@example.com is already registered.');
  });

  function enterValue(label: string, value: string): void {
    const input = controlLabelledBy(label) as HTMLInputElement;
    input.value = value;
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  }

  function choosePlan(plan: 'STANDARD' | 'PREMIUM'): void {
    const input = fixture.nativeElement.querySelector(
      `input[type="radio"][value="${plan}"]`,
    ) as HTMLInputElement;
    input.click();
    fixture.detectChanges();
  }

  function controlLabelledBy(text: string): HTMLInputElement | HTMLSelectElement {
    const labels = Array.from(
      fixture.nativeElement.querySelectorAll('label'),
    ) as HTMLLabelElement[];
    const label = labels.find((candidate) => candidate.textContent?.includes(text));
    const control = label?.htmlFor
      ? fixture.nativeElement.querySelector(`#${label.htmlFor}`)
      : label?.querySelector('input, select');
    if (!control) {
      throw new Error(`No control labelled "${text}" was found.`);
    }
    return control;
  }

  function buttonNamed(text: string): HTMLButtonElement {
    const buttons = Array.from(
      fixture.nativeElement.querySelectorAll('button'),
    ) as HTMLButtonElement[];
    const button = buttons.find((candidate) => candidate.textContent?.includes(text));
    if (!button) {
      throw new Error(`No button named "${text}" was found.`);
    }
    return button;
  }

  function pageText(): string {
    return fixture.nativeElement.textContent;
  }
});
