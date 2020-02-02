export interface AdjustAttribution {
    trackerToken: string
    trackerName: string
    network: string
    campaign: string
    adgroup: string
    creative: string
    clickLabel: string
    adid: string
  }

export type AdjustListener = (attribution: AdjustAttribution) => Promise<void>;

declare const AdjustIntegration:
    | {disabled: true; setupListener(): void; getAttribution(): void}
    | { 
        (): Promise<void>;
        setupListener(token: string, environment: string, listener: AdjustListener): void;
        getAttribution(listener: AdjustListener): void;
    }

export = AdjustIntegration
