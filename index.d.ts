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

type Listener = (attribution: AdjustAttribution) => Promise<void>;

declare const AdjustIntegration:
    | {disabled: true, setupListener(): void}
    | { 
        (): Promise<void>;
        setupListener(token: string, environment: string, listener: Listener): void;
    }

export = AdjustIntegration
