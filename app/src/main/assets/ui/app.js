(function() {
    'use strict';

    var $ = function(id) {
        return document.getElementById(id);
    };

    window.setFps = function(value) {
        var fpsEl = $('fps');
        if (fpsEl) {
            fpsEl.textContent = value + ' FPS';
            fpsEl.classList.add('pulse');
            setTimeout(function() {
                fpsEl.classList.remove('pulse');
            }, 200);
        }
    };

    window.updateDeviceInfo = function(info) {
        var el = $('deviceInfo');
        if (el) {
            el.textContent = info;
        }
    };

    window.updateFromNative = function(settings) {
        var resScale = $('resolutionScale');
        var resVal = $('resVal');
        var fpsCap = $('fpsCap');
        var fpsCapVal = $('fpsCapVal');
        var fpsBoost = $('fpsBoost');
        var reduceEffects = $('reduceEffects');
        var cpuOpt = $('cpuOpt');

        if (resScale && resVal) {
            resScale.value = settings.resolutionScale;
            resVal.textContent = settings.resolutionScale.toFixed(2);
        }

        if (fpsCap && fpsCapVal) {
            fpsCap.value = settings.fpsCap;
            fpsCapVal.textContent = settings.fpsCap;
        }

        if (fpsBoost) {
            fpsBoost.checked = settings.fpsCap > 0;
        }

        if (reduceEffects) {
            reduceEffects.checked = settings.reduceEffects;
        }

        if (cpuOpt) {
            cpuOpt.checked = settings.cpuOptimization;
        }
    };

    function setupEventListeners() {
        var resScale = $('resolutionScale');
        var resVal = $('resVal');
        var fpsCap = $('fpsCap');
        var fpsCapVal = $('fpsCapVal');
        var fpsBoost = $('fpsBoost');
        var reduceEffects = $('reduceEffects');
        var cpuOpt = $('cpuOpt');
        var launchBtn = $('launchBtn');

        if (resScale && resVal) {
            resScale.addEventListener('input', function(e) {
                var val = parseFloat(e.target.value);
                resVal.textContent = val.toFixed(2);
                if (window.Android) {
                    window.Android.setResolutionScale(val);
                }
            });
        }

        if (fpsCap && fpsCapVal) {
            fpsCap.addEventListener('input', function(e) {
                var val = parseInt(e.target.value);
                fpsCapVal.textContent = val;
                if (window.Android) {
                    window.Android.setFpsCap(val);
                }
            });
        }

        if (fpsBoost) {
            fpsBoost.addEventListener('change', function(e) {
                var cap = e.target.checked ? 120 : 0;
                if (fpsCap) fpsCap.value = cap;
                if (fpsCapVal) fpsCapVal.textContent = cap;
                if (window.Android) {
                    window.Android.setFpsCap(cap);
                }
            });
        }

        if (reduceEffects) {
            reduceEffects.addEventListener('change', function(e) {
                if (window.Android) {
                    window.Android.setReduceEffects(e.target.checked);
                }
            });
        }

        if (cpuOpt) {
            cpuOpt.addEventListener('change', function(e) {
                if (window.Android) {
                    window.Android.setCpuOptimization(e.target.checked);
                }
            });
        }
    }

    function setupPresets() {
        var buttons = document.querySelectorAll('[data-preset]');
        var resScale = $('resolutionScale');
        var resVal = $('resVal');
        var fpsCap = $('fpsCap');
        var fpsCapVal = $('fpsCapVal');
        var fpsBoost = $('fpsBoost');
        var reduceEffects = $('reduceEffects');
        var cpuOpt = $('cpuOpt');

        var presets = {
            low: { resScale: 0.5, fpsCap: 60, reduceEffects: true, cpuOpt: true },
            balanced: { resScale: 0.75, fpsCap: 90, reduceEffects: false, cpuOpt: true },
            ultra: { resScale: 1.0, fpsCap: 0, reduceEffects: false, cpuOpt: false }
        };

        buttons.forEach(function(btn) {
            btn.addEventListener('click', function() {
                var preset = presets[btn.dataset.preset];
                if (!preset) return;

                if (resScale) resScale.value = preset.resScale;
                if (resVal) resVal.textContent = preset.resScale.toFixed(2);
                if (fpsCap) fpsCap.value = preset.fpsCap;
                if (fpsCapVal) fpsCapVal.textContent = preset.fpsCap;
                if (fpsBoost) fpsBoost.checked = preset.fpsCap > 0;
                if (reduceEffects) reduceEffects.checked = preset.reduceEffects;
                if (cpuOpt) cpuOpt.checked = preset.cpuOpt;

                if (window.Android) {
                    window.Android.setResolutionScale(preset.resScale);
                    window.Android.setFpsCap(preset.fpsCap);
                    window.Android.setReduceEffects(preset.reduceEffects);
                    window.Android.setCpuOptimization(preset.cpuOpt);
                }
            });
        });
    }

    function setupLaunchButton() {
        var btn = $('launchBtn');
        if (btn && window.Android) {
            btn.addEventListener('click', function() {
                window.Android.launchMinecraft();
            });
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            setupEventListeners();
            setupPresets();
            setupLaunchButton();
        });
    } else {
        setupEventListeners();
        setupPresets();
        setupLaunchButton();
    }
})();