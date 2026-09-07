import type { Meta, StoryObj } from "@storybook/angular";

import { ButtonComponent } from "../button/button";
import { DividerComponent } from "./divider";

const meta: Meta<DividerComponent> = {
  argTypes: {
    decorative: { control: "boolean" },
    orientation: {
      control: "select",
      options: ["horizontal", "vertical"],
    },
    thickness: {
      control: "select",
      options: ["thin", "default", "thick"],
    },
    variant: {
      control: "select",
      options: ["default", "strong", "subtle"],
    },
  },
  component: DividerComponent,
  render: (args) => ({
    props: args,
    template: `
      <div style="width: 300px; padding: 16px;">
        <p style="margin-bottom: 8px; font-size: 14px;">Contenido Superior</p>
        <nv-divider
          [orientation]="orientation"
          [thickness]="thickness"
          [variant]="variant"
          [decorative]="decorative"
        />
        <p style="margin-top: 8px; font-size: 14px;">Contenido Inferior</p>
      </div>
    `,
  }),
  tags: ["autodocs"],
  title: "Components/Divider",
};

export default meta;
type Story = StoryObj<DividerComponent>;

export const Horizontal: Story = {
  args: {
    decorative: true,
    orientation: "horizontal",
    thickness: "default",
    variant: "default",
  },
};

export const Vertical: Story = {
  args: {
    decorative: true,
    orientation: "vertical",
    thickness: "default",
    variant: "default",
  },
  render: (args) => ({
    props: args,
    template: `
      <div class="flex items-center gap-3 p-4">
        <span class="text-sm font-medium">Inicio</span>
        <nv-divider
          [orientation]="orientation"
          [thickness]="thickness"
          [variant]="variant"
          [decorative]="decorative"
        />
        <span class="text-sm font-medium">Operaciones</span>
        <nv-divider
          [orientation]="orientation"
          [thickness]="thickness"
          [variant]="variant"
          [decorative]="decorative"
        />
        <span class="text-sm font-medium">Configuración</span>
      </div>
    `,
  }),
};

export const ThicknessOptions: StoryObj = {
  render: () => ({
    moduleMetadata: { imports: [DividerComponent] },
    template: `
      <div class="flex flex-col gap-6 p-4 max-w-md">
        <div>
          <span class="text-xs text-muted-foreground block mb-2 font-medium">Thin (1px)</span>
          <nv-divider thickness="thin" />
        </div>
        <div>
          <span class="text-xs text-muted-foreground block mb-2 font-medium">Default (2px)</span>
          <nv-divider thickness="default" />
        </div>
        <div>
          <span class="text-xs text-muted-foreground block mb-2 font-medium">Thick (3px)</span>
          <nv-divider thickness="thick" />
        </div>
        <div class="pt-4 border-t border-[var(--border)]">
          <span class="text-xs text-muted-foreground block mb-4 font-medium">Vertical Thicknesses</span>
          <div class="flex items-center gap-6 h-10">
            <div class="flex items-center gap-2">
              <span class="text-xs text-muted-foreground">Thin</span>
              <nv-divider orientation="vertical" thickness="thin" />
            </div>
            <div class="flex items-center gap-2">
              <span class="text-xs text-muted-foreground">Default</span>
              <nv-divider orientation="vertical" thickness="default" />
            </div>
            <div class="flex items-center gap-2">
              <span class="text-xs text-muted-foreground">Thick</span>
              <nv-divider orientation="vertical" thickness="thick" />
            </div>
          </div>
        </div>
      </div>
    `,
  }),
};

export const Variants: StoryObj = {
  render: () => ({
    moduleMetadata: { imports: [DividerComponent] },
    template: `
      <div class="flex flex-col gap-6 p-4 max-w-md">
        <div>
          <span class="text-xs text-muted-foreground block mb-2 font-medium">Default</span>
          <nv-divider variant="default" />
        </div>
        <div>
          <span class="text-xs text-muted-foreground block mb-2 font-medium">Strong</span>
          <nv-divider variant="strong" />
        </div>
        <div>
          <span class="text-xs text-muted-foreground block mb-2 font-medium">Subtle</span>
          <nv-divider variant="subtle" />
        </div>
      </div>
    `,
  }),
};

export const WithButtonToolbar: StoryObj = {
  render: () => ({
    moduleMetadata: { imports: [DividerComponent, ButtonComponent] },
    template: `
      <div class="flex items-center gap-2 p-3 border border-[var(--border)] rounded-lg">
        <nv-button variant="outline">Ver Tickets</nv-button>
        <nv-divider orientation="vertical" class="mx-2 h-7" />
        <nv-button>Registrar Ingreso</nv-button>
        <nv-button variant="secondary">Procesar Salida</nv-button>
      </div>
    `,
  }),
};
