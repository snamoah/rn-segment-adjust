declare const AdjustIntegration:
    | {disabled: true}
    | (() => Promise<void>)

export = AdjustIntegration
