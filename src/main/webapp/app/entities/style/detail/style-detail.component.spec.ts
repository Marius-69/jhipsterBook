import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StyleDetailComponent } from './style-detail.component';

describe('Style Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StyleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StyleDetailComponent,
              resolve: { style: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StyleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load style on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StyleDetailComponent);

      // THEN
      expect(instance.style).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
