import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StyleDeuxDetailComponent } from './style-deux-detail.component';

describe('StyleDeux Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StyleDeuxDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StyleDeuxDetailComponent,
              resolve: { styleDeux: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StyleDeuxDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load styleDeux on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StyleDeuxDetailComponent);

      // THEN
      expect(instance.styleDeux).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
