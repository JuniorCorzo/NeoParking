import { signal } from "@angular/core";
import { createColumnHelper } from "@tanstack/angular-table";

import { DataTableState } from "./data-table.state";

interface TestRow {
  category: string;
  id: string;
  name: string;
}

describe("DataTableState", () => {
  let state: DataTableState<TestRow>;
  const data = signal<TestRow[]>([
    { category: "fruit", id: "1", name: "Apple" },
    { category: "fruit", id: "2", name: "Banana" },
    { category: "vegetable", id: "3", name: "Carrot" },
  ]);

  const columnHelper = createColumnHelper<TestRow>();

  beforeEach(() => {
    state = new DataTableState<TestRow>();
    state.createTable({
      columns: [
        columnHelper.accessor("name", {
          header: "Name",
          id: "name",
        }),
        columnHelper.accessor("category", {
          header: "Category",
          id: "category",
        }),
      ],
      data: () => data(),
    });
  });

  it("should update globalFilter and reset pagination page index", () => {
    state.pagination.set({ pageIndex: 2, pageSize: 10 });
    state.setGlobalFilter("test");

    expect(state.globalFilter()).toBe("test");
    expect(state.pagination().pageIndex).toBe(0);
  });

  it("should set and clear column filter", () => {
    state.setColumnFilter("category", "fruit");
    expect(state.getColumnFilterValue("category")).toBe("fruit");

    state.setColumnFilter("category", "ALL");
    expect(state.getColumnFilterValue("category")).toBeUndefined();
  });

  it("should reset all filters and pagination", () => {
    state.setGlobalFilter("search");
    state.setColumnFilter("category", "fruit");
    state.pagination.set({ pageIndex: 3, pageSize: 10 });

    state.resetFilters();

    expect(state.globalFilter()).toBe("");
    expect(state.columnFilters()).toEqual([]);
    expect(state.pagination().pageIndex).toBe(0);
  });
});
