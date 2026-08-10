import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Membership, MembershipPage, MembershipSearch, RegisterMembership } from './membership';

@Injectable({ providedIn: 'root' })
export class MembershipsApi {
  private readonly http = inject(HttpClient);

  list(search: MembershipSearch): Observable<MembershipPage> {
    let params = new HttpParams().set('page', search.page).set('size', search.size);

    if (search.status) {
      params = params.set('status', search.status);
    }

    return this.http.get<MembershipPage>('/api/admin/memberships', { params });
  }

  get(membershipId: string): Observable<Membership> {
    return this.http.get<Membership>(`/api/memberships/${membershipId}`);
  }

  register(request: RegisterMembership): Observable<Membership> {
    return this.http.post<Membership>('/api/memberships', request);
  }

  pause(membershipId: string, durationInDays: number): Observable<Membership> {
    return this.http.post<Membership>(`/api/memberships/${membershipId}/pause`, {
      durationInDays,
    });
  }

  resume(membershipId: string): Observable<Membership> {
    return this.http.post<Membership>(`/api/memberships/${membershipId}/resume`, null);
  }
}
