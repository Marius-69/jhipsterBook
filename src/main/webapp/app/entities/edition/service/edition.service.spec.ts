import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEdition } from '../edition.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../edition.test-samples';

import { EditionService } from './edition.service';

const requireRestSample: IEdition = {
  ...sampleWithRequiredData,
};

describe('Edition Service', () => {
  let service: EditionService;
  let httpMock: HttpTestingController;
  let expectedResult: IEdition | IEdition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EditionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Edition', () => {
      const edition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(edition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Edition', () => {
      const edition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(edition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Edition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Edition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Edition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEditionToCollectionIfMissing', () => {
      it('should add a Edition to an empty array', () => {
        const edition: IEdition = sampleWithRequiredData;
        expectedResult = service.addEditionToCollectionIfMissing([], edition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(edition);
      });

      it('should not add a Edition to an array that contains it', () => {
        const edition: IEdition = sampleWithRequiredData;
        const editionCollection: IEdition[] = [
          {
            ...edition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEditionToCollectionIfMissing(editionCollection, edition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Edition to an array that doesn't contain it", () => {
        const edition: IEdition = sampleWithRequiredData;
        const editionCollection: IEdition[] = [sampleWithPartialData];
        expectedResult = service.addEditionToCollectionIfMissing(editionCollection, edition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(edition);
      });

      it('should add only unique Edition to an array', () => {
        const editionArray: IEdition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const editionCollection: IEdition[] = [sampleWithRequiredData];
        expectedResult = service.addEditionToCollectionIfMissing(editionCollection, ...editionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const edition: IEdition = sampleWithRequiredData;
        const edition2: IEdition = sampleWithPartialData;
        expectedResult = service.addEditionToCollectionIfMissing([], edition, edition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(edition);
        expect(expectedResult).toContain(edition2);
      });

      it('should accept null and undefined values', () => {
        const edition: IEdition = sampleWithRequiredData;
        expectedResult = service.addEditionToCollectionIfMissing([], null, edition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(edition);
      });

      it('should return initial array if no Edition is added', () => {
        const editionCollection: IEdition[] = [sampleWithRequiredData];
        expectedResult = service.addEditionToCollectionIfMissing(editionCollection, undefined, null);
        expect(expectedResult).toEqual(editionCollection);
      });
    });

    describe('compareEdition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEdition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEdition(entity1, entity2);
        const compareResult2 = service.compareEdition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEdition(entity1, entity2);
        const compareResult2 = service.compareEdition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEdition(entity1, entity2);
        const compareResult2 = service.compareEdition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
