import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EditionDetailComponent } from './edition-detail.component';

describe('Edition Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EditionDetailComponent,
              resolve: { edition: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EditionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load edition on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EditionDetailComponent);

      // THEN
      expect(instance.edition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
