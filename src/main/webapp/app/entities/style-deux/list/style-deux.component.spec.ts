import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StyleDeuxService } from '../service/style-deux.service';

import { StyleDeuxComponent } from './style-deux.component';

describe('StyleDeux Management Component', () => {
  let comp: StyleDeuxComponent;
  let fixture: ComponentFixture<StyleDeuxComponent>;
  let service: StyleDeuxService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'style-deux', component: StyleDeuxComponent }]),
        HttpClientTestingModule,
        StyleDeuxComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(StyleDeuxComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StyleDeuxComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StyleDeuxService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.styleDeuxes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to styleDeuxService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getStyleDeuxIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getStyleDeuxIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
