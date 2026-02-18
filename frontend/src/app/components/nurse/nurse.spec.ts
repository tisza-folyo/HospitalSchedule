import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Nurse } from './nurse';

describe('Nurse', () => {
  let component: Nurse;
  let fixture: ComponentFixture<Nurse>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Nurse]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Nurse);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
